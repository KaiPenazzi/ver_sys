use std::{io, sync::Arc};
use tokio::{net::UdpSocket, sync::Mutex};

use crate::{
    game::Game,
    model::messages::{ActionData, InitData, Message},
};

pub struct Server {
    url: String,
    game: Arc<Mutex<Game>>,
}

impl Server {
    pub fn new(url: String, game: Arc<Mutex<Game>>) -> Self {
        Self {
            url: url,
            game: game,
        }
    }

    pub async fn start(&self) -> io::Result<()> {
        let socket = UdpSocket::bind(self.url.clone()).await?;

        let mut buf = [0; 1024];
        loop {
            let (_len, _addr) = socket.recv_from(&mut buf).await?;
            self.handle_message(&buf).await;
        }
    }

    async fn handle_message(&self, message: &[u8]) {
        let message: Message = serde_json::from_slice(message).unwrap();

        match message.r#type.as_str() {
            "init" => {
                if let Ok(init) = serde_json::from_value::<InitData>(message.data) {
                    println!("Init Data: {:?}", init);
                } else {
                    eprintln!("Fehler beim Parsen von 'init' Daten");
                }
            }
            "action" => {
                if let Ok(action) = serde_json::from_value::<ActionData>(message.data) {
                    let mut game = self.game.lock().await;
                    game.set(&action);
                } else {
                    eprintln!("Fehler beim Parsen von 'action' Daten");
                }
            }
            _ => {
                eprintln!("Unbekannter Nachrichtentyp: {}", message.r#type);
            }
        }
    }
}
