pub mod field;
pub mod scores;

use druid::{Data, Lens};
use field::GameField;
use scores::GameScores;

#[derive(Clone, Data, Lens)]
pub struct Game {
    pub field: GameField,
    pub scores: GameScores,
}

impl Game {
    pub fn new(x: u32, y: u32, k: u32) -> Self {
        Self {
            field: GameField::init(x, y, k),
            scores: GameScores::new(),
        }
    }

    pub fn check(&mut self) {
        match self.field.check() {
            Some(usr) => self.scores.add_point(usr),
            None => (),
        }
    }
}
