pub mod field;
pub mod scores;

use druid::{Data, Lens};
use field::GameField;
use scores::GameScores;

use crate::model::messages::InitData;

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

    pub fn from_init(init: InitData) -> Self {
        Self {
            field: GameField::init_with_fields(init.field, init.k),
            scores: GameScores::from_vec(init.score),
        }
    }

    pub fn check(&mut self) {
        match self.field.check() {
            Some(usr) => self.scores.add_point(usr),
            None => (),
        }
    }

    pub fn to_init(&self) -> InitData {
        InitData {
            r#type: "init".to_string(),
            field: self.field.to_vec(),
            score: self.scores.to_vec(),
            k: self.field.k,
        }
    }
}
