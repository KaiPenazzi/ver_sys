use druid::{im::Vector, Data, Lens};
use serde::{Deserialize, Serialize};

#[derive(Clone, Data, Lens)]
pub struct GameScores {
    scores: Vector<Score>,
}

impl GameScores {
    pub fn new() -> Self {
        Self {
            scores: Vector::new(),
        }
    }

    pub fn from_vec(vec: Vec<Score>) -> Self {
        Self {
            scores: Vector::from(vec),
        }
    }

    pub fn add_point(&mut self, usr: String) {
        if let Some(score) = self.scores.iter_mut().find(|s| s.usr == usr) {
            score.points += 1;
        } else {
            self.scores.push_back(Score::new(usr));
        }
    }

    pub fn to_vec(&self) -> Vec<Score> {
        self.scores.clone().into_iter().collect()
    }
}

#[derive(Clone, Data, Lens, Serialize, Deserialize, Debug)]
pub struct Score {
    pub usr: String,
    pub points: u32,
}

impl Score {
    pub fn new(usr: String) -> Self {
        Self {
            usr: usr,
            points: 1,
        }
    }
}
