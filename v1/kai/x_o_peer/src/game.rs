pub mod field;
pub mod scores;

use druid::{Data, Lens};
use field::GameField;
use scores::GameScores;

use crate::model::messages::ActionData;

#[derive(Clone, Data, Lens)]
pub struct Game {
    pub field: GameField,
}

impl Game {
    pub fn new(x: u32, y: u32, k: u32) -> Self {
        Self {
            field: GameField::init(x, y, k),
        }
    }
}

//#[cfg(test)]
//mod test_game {
//    use super::Game;
//
//    #[test]
//    fn test_new() {
//        let game = Game::new(3, 3, 3);
//
//        assert_eq!(game.field.field.len(), 3);
//        for x in game.field.field {
//            assert_eq!(x.len(), 3);
//        }
//    }
//}
