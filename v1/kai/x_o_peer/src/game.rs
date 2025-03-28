pub mod field;
pub mod scores;

use field::GameField;
use scores::GameScores;

use crate::model::messages::ActionData;

pub struct Game {
    pub scores: GameScores,
    pub field: GameField,
}

impl Game {
    pub fn new(x: u32, y: u32, k: u32) -> Self {
        Self {
            scores: GameScores::new(),
            field: GameField::init(x, y, k),
        }
    }

    pub fn set(&mut self, action: &ActionData) {
        self.field.set(action);
        println!("Action Data: {:?}", action);
    }
}

#[cfg(test)]
mod test_game {
    use super::Game;

    #[test]
    fn test_new() {
        let game = Game::new(3, 3, 3);

        assert_eq!(game.field.field.len(), 3);
        for x in game.field.field {
            assert_eq!(x.len(), 3);
        }
    }
}
