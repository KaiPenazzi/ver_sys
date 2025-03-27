use std::future::Future;
use std::sync::Arc;

use tokio::sync::Mutex;

use crate::game::Game;
use crate::model::messages::ActionData;
use crate::udp::{client::Client, server::Server};

pub struct Manager {
    game: Option<Arc<Mutex<Game>>>,
    udp_server: Option<Server>,
    udp_client: Option<Client>,
}

impl Manager {
    pub fn new() -> Self {
        Self {
            game: None,
            udp_server: None,
            udp_client: None,
        }
    }

    pub fn start_game(&mut self, port: i32, x: usize, y: usize, k: usize) {
        println!("pressed start game");
        let game = Game::new(x, y, k);
        let arc_game = Arc::new(Mutex::new(game));
        self.udp_server = Some(Server::new(
            format!("0.0.0.0:{}", port),
            Arc::clone(&arc_game),
        ));
        self.game = Some(arc_game);
    }

    pub async fn action(&mut self, usr: String, x: usize, y: usize) {
        match &self.game {
            Some(game) => {
                let mut unlock_game = game.lock().await;
                unlock_game.set(&ActionData {
                    usr: usr,
                    x: x,
                    y: y,
                })
            }
            None => println!("no game is started"),
        }
    }
}
