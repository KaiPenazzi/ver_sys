use druid::{im::Vector, Data, Lens};

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

    pub fn add_point(&mut self, usr: String) {
        if let Some(score) = self.scores.iter_mut().find(|s| s.usr == usr) {
            score.points += 1;
        } else {
            self.scores.push_back(Score::new(usr));
        }
    }
}

#[derive(Clone, Data, Lens)]
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
