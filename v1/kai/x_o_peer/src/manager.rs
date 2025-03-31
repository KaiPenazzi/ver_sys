use druid::{im::Vector, Data, Env, EventCtx, Lens};
use std::sync::{
    mpsc::{Receiver, Sender},
    Arc, Mutex,
};

use crate::{
    game::Game,
    model::{
        com::{Peer, SendMsg},
        messages::{ActionData, InitData, Message},
    },
};

#[derive(Clone, Data, Lens)]
pub struct Manager {
    pub usr: String,
    pub game: Game,
    pub msg_q: Arc<Mutex<Sender<SendMsg>>>,
    pub peers: Vector<Peer>,
    pub x_size: String,
    pub y_size: String,
    pub k_size: String,
}

impl Manager {
    pub fn new(usr: String, rx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        Self {
            game: Game::new(0, 0, 0),
            usr: usr,
            msg_q: rx,
            peers: Vector::new(),
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
        let tx = self.msg_q.lock().unwrap();
        println!("send init");
        tx.send(SendMsg {
            msg: Message::init(InitData {
                field: self.game.field.to_vec(),
                k: self.game.field.k,
            }),
            send_to: vec![Peer::new("127.0.0.1".to_string(), 1234)],
        })
        .unwrap();
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
            msg: Message::action(action),
            send_to: vec![Peer::new("127.0.0.1".to_string(), 1234)],
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
