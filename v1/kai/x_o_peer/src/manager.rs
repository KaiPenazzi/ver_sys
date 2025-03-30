use druid::{Data, Lens};

use crate::{
    game::Game,
    model::messages::{ActionData, InitData, Message},
};

#[derive(Clone, Data, Lens)]
pub struct Manager {
    pub usr: String,
    pub game: Game,
}

impl Manager {
    pub fn new(usr: String) -> Self {
        Self {
            game: Game::new(&usr, 4, 4, 3),
            usr: usr,
        }
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
                    self.game.field.set(action);
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
