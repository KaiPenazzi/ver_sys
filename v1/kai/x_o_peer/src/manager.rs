use std::error::Error;
use std::future::Future;
use std::sync::Arc;

use tokio::sync::Mutex;
use tokio::task::{JoinError, JoinHandle};

use crate::game::Game;
use crate::model::messages::ActionData;
use crate::udp::{client::Client, server::Server};

pub struct Manager {
    pub usr: String,
    game: Option<Arc<Mutex<Game>>>,
    udp_client: Option<Client>,
    udp_server_task: Option<Arc<JoinHandle<()>>>,
}

impl Manager {
    pub fn new(usr: String) -> Self {
        Self {
            usr: usr,
            game: None,
            udp_client: None,
            udp_server_task: None,
        }
    }

    pub fn start_game(&mut self, port: i32, x: u32, y: u32, k: u32) {
        println!("start game");
        let game = Game::new(x, y, k);
        let arc_game = Arc::new(Mutex::new(game));
        let udp_server = Server::new(format!("0.0.0.0:{}", port), Arc::clone(&arc_game));

        self.game = Some(arc_game);
        self.udp_server_task = Some(Arc::new(tokio::spawn(async move {
            _ = udp_server.start().await;
        })));
    }

    pub fn stop(&mut self) {
        match &self.udp_server_task {
            Some(handle) => {
                let copy_handle = Arc::clone(&handle);
                tokio::spawn(async move {
                    copy_handle.abort();
                });
            }
            None => println!("upd_server is not running"),
        }
    }

    pub fn action(&mut self, x: u32, y: u32) {
        match &self.game {
            Some(game) => {
                let game_clone = Arc::clone(&game);
                let user = self.usr.clone();

                tokio::spawn(async move {
                    let mut unlock_game = game_clone.lock().await;
                    unlock_game.set(&ActionData {
                        usr: user,
                        x: x,
                        y: y,
                    });
                });
            }
            None => println!("no game is started"),
        }
    }
}
