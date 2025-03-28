use crate::game::Game;
use crate::model::messages::ActionData;
use crate::udp::client::Client;

pub struct Manager {
    pub usr: String,
    pub game: Option<Game>,
    udp_client: Option<Client>,
}

impl Manager {
    pub fn new(usr: String) -> Self {
        Self {
            usr: usr,
            game: None,
            udp_client: None,
        }
    }

    pub fn start_game(&mut self, x: u32, y: u32, k: u32) {
        println!("start game");
        let game = Game::new(x, y, k);
        self.game = Some(game);
    }

    pub fn action(&mut self, x: u32, y: u32) {
        match &mut self.game {
            Some(game) => game.set(&ActionData {
                usr: self.usr.clone(),
                x: x,
                y: y,
            }),
            None => println!("no game is started"),
        }
    }
}
