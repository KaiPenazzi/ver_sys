use std::collections::HashMap;

use druid::{im::Vector, Data, Lens};

#[derive(Clone, Data, Lens)]
pub struct GameScores {
    scores: Vector<Score>,
}

#[derive(Clone, Data, Lens)]
pub struct Score {
    name: String,
    points: u32,
}

impl GameScores {}
