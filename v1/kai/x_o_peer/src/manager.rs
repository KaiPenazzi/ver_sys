use druid::{Data, Lens};
use std::sync::{
    mpsc::{Receiver, Sender},
    Arc, Mutex,
};

use crate::{
    game::Game,
    model::messages::{ActionData, InitData, Message, Peer, SendMsg},
};

#[derive(Clone, Data, Lens)]
pub struct Manager {
    pub usr: String,
    pub game: Game,
    pub msg_q: Arc<Mutex<Sender<SendMsg>>>,
}

impl Manager {
    pub fn new(usr: String, rx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        Self {
            game: Game::new(4, 4, 3),
            usr: usr,
            msg_q: rx,
        }
    }

    pub fn action(&mut self, x: u32, y: u32) {
        let action = ActionData {
            usr: self.usr.clone(),
            x: x,
            y: y,
        };
        self.game.field.set(&action);
        self.game.check();
        let tx = self.msg_q.lock().unwrap();
        tx.send(SendMsg {
            msg: Message {
                r#type: "action".to_string(),
                data: serde_json::json!(&action),
            },
            send_to: vec![Peer::new("127.0.0.1".to_string(), 1225)],
        })
        .unwrap();
    }

    pub fn rec_msg(&mut self, msg: &Message) {
        self.parser(msg)
    }

    fn parser(&mut self, msg: &Message) {
        match msg.r#type.as_str() {
            "init" => {
                if let Ok(init) = serde_json::from_value::<InitData>(msg.data.clone()) {
                    println!("Init Data: {:?}", init);
                } else {
                    eprintln!("Fehler beim Parsen von 'init' Daten");
                }
            }
            "action" => {
                if let Ok(action) = serde_json::from_value::<ActionData>(msg.data.clone()) {
                    self.game.field.set(&action);
                    self.game.check();
                } else {
                    eprintln!("Fehler beim Parsen von 'action' Daten");
                }
            }
            _ => {
                eprintln!("Unbekannter Nachrichtentyp: {}", msg.r#type);
            }
        }
    }
}
