use std::collections::HashMap;

use druid::Data;

pub struct GameScores {
    scores: HashMap<String, u32>,
}

impl GameScores {
    pub fn new() -> Self {
        Self {
            scores: HashMap::new(),
        }
    }

    pub fn inc(&mut self, player: &str) {
        let entry = self.scores.entry(player.to_string()).or_insert(0);
        *entry += 1;
    }

    pub fn get(&self, player: &str) -> Option<&u32> {
        self.scores.get(player)
    }
}

#[cfg(test)]
mod test_score {
    use super::GameScores;

    #[test]
    fn test_new() {
        let mut scores = GameScores::new();
        scores.inc("hans");

        assert_eq!(scores.get("hans"), Some(&1))
    }

    #[test]
    fn test_score() {
        let mut scores = GameScores::new();
        scores.inc("hans");
        scores.inc("hans");

        assert_eq!(scores.get("hans"), Some(&2))
    }
}
