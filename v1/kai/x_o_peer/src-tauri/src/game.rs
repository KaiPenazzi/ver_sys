pub mod field;
pub mod scores;

use field::GameField;
use scores::GameScores;

use crate::model::messages::ActionData;

pub struct Game {
    pub scores: GameScores,
    pub fields: GameField,
}

impl Game {
    pub fn new(x: usize, y: usize, k: usize) -> Self {
        Self {
            scores: GameScores::new(),
            fields: GameField::init(x, y, k),
        }
    }

    pub fn set(&mut self, action: &ActionData) {
        self.fields.set(action);
        println!("Action Data: {:?}", action);
    }
}

#[cfg(test)]
mod test_game {
    use super::Game;

    #[test]
    fn test_new() {
        let game = Game::new(3, 3, 3);

        assert_eq!(game.fields.field.len(), 3);
        for x in game.fields.field {
            assert_eq!(x.len(), 3);
        }
    }
}
