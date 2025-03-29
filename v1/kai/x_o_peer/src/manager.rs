use druid::{Data, Lens};

use crate::game::Game;

#[derive(Clone, Data, Lens)]
pub struct Manager {
    pub usr: String,
    pub game: Game,
}

impl Manager {
    pub fn new(usr: String) -> Self {
        Self {
            usr: usr,
            game: Game::new(3, 3, 3),
        }
    }
}
