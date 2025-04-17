use druid::{Data, Lens};
use std::sync::{mpsc::Sender, Arc, Mutex};

use crate::{
    game::Game,
    model::{
        com::{RecvMsg, SendMsg},
        messages::{ActionData, Message},
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
    pub fn new(usr: String, urls: Vec<String>, tx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        Self {
            game: Game::new(0, 0, 0),
            usr: usr,
            msq_client: Client::new(tx, urls),
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
            Message::Join(_join) => {
                self.msq_client
                    .send_init(self.game.to_init(), Some(udp_msg.from.clone()));
            }
        }
    }
}
