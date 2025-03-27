use crate::model::game_structs::{GameField, GameScores};

pub struct Game {
    pub scores: GameScores,
    pub fields: GameField,
}

impl Game {
    pub fn new() -> Self {
        Self {
            scores: GameScores::new(),
            fields: GameField::init(3, 3),
        }
    }
}

#[cfg(test)]
mod test_game {
    use super::Game;

    #[test]
    fn test_new() {
        let game = Game::new();

        assert_eq!(game.fields.field.len(), 3);
        for x in game.fields.field {
            assert_eq!(x.len(), 3);
        }
    }
}
