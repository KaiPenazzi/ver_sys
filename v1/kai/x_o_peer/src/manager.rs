use druid::{Data, Lens};
use std::sync::{mpsc::Sender, Arc, Mutex};

use crate::{
    game::Game,
    model::{
        com::{Peer, RecvMsg, SendMsg},
        messages::{ActionData, Message, ToPeer},
    },
    udp::client::Client,
};

#[derive(Clone, Data, Lens)]
pub struct Manager {
    pub usr: String,
    pub game: Game,
    pub msq_client: Client,
    pub x_size: String,
    pub y_size: String,
    pub k_size: String,
}

impl Manager {
    pub fn new(usr: String, tx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        Self {
            game: Game::new(0, 0, 0),
            usr: usr,
            msq_client: Client::new(tx),
            x_size: 3.to_string(),
            y_size: 3.to_string(),
            k_size: 3.to_string(),
        }
    }

    pub fn new_game(&mut self) {
        self.game = Game::new(
            self.x_size.parse::<u32>().unwrap(),
            self.y_size.parse::<u32>().unwrap(),
            self.k_size.parse::<u32>().unwrap(),
        );
        self.msq_client.send_init(self.game.to_init(), None)
    }

    pub fn action(&mut self, x: u32, y: u32) {
        let action = ActionData {
            r#type: "action".to_string(),
            usr: self.usr.clone(),
            x: x,
            y: y,
        };
        self.game.field.set(&action);
        self.game.check();
        self.msq_client.send_action(action)
    }

    pub fn join(&self) {
        self.msq_client.send_join();
    }

    pub fn rec_msg(&mut self, msg: &RecvMsg) {
        self.parser(msg)
    }

    fn parser(&mut self, udp_msg: &RecvMsg) {
        let msg = &udp_msg.msg;
        match msg {
            Message::Init(init) => {
                self.game = Game::from_init(init.clone());
            }
            Message::Action(action) => {
                self.game.field.set(&action);
                self.game.check();
            }
            Message::Join(join) => {
                let new_peer = join.to_peer();
                self.msq_client
                    .send_init(self.game.to_init(), Some(&new_peer));
                self.msq_client.add(new_peer);
            }
            Message::Leave(leave) => {
                let leave_peer = leave.to_peer();
                self.msq_client.leave(&leave_peer);
            }
            Message::NewPlayer(new_player) => {
                let new_peer = new_player.to_peer();
                self.msq_client.add(new_peer);
            }
        }
    }
}
