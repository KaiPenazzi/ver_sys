use druid::{Data, Lens};
use std::{
    net::SocketAddr,
    str::FromStr,
};

use crate::{
    game::Game,
    model::{
        com::RecvMsg,
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
    pub friend: String,
}

impl Manager {
    pub fn new(usr: String, msq_client: Client) -> Self {
        Self {
            game: Game::new(0, 0, 0),
            usr,
            msq_client,
            x_size: 3.to_string(),
            y_size: 3.to_string(),
            k_size: 3.to_string(),
            friend: "".to_string(),
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
            x,
            y,
        };
        self.game.field.set(&action);
        self.game.check();
        self.msq_client.send_action(action)
    }

    pub fn join(&self) {
        let url = SocketAddr::from_str(&self.friend);
        match url {
            Ok(url) => {
                self.msq_client.send_join(&url);
            }
            Err(_) => {
                println!("Invalid URL");
                return;
            }
        }
    }

    pub fn leave(&mut self) {
        self.msq_client.send_leave();
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
                self.msq_client.send_player(&new_peer);
                self.msq_client.send_new_player(&join);
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
            Message::Player(player) => {
                self.msq_client.add_players(player.clone());
            }
        }
    }
}
