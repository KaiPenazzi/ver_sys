use serde::{Deserialize, Serialize};
use serde_json::json;

use crate::game::scores::Score;

#[derive(Debug, Deserialize, Serialize)]
pub enum Message {
    Init(InitData),
    Action(ActionData),
    Join(JoinData),
}

#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct InitData {
    pub r#type: String,
    pub field: Vec<Vec<String>>,
    pub score: Vec<Score>,
    pub k: u32,
}

impl InitData {
    pub fn to_msg(self) -> Message {
        Message::Init(self)
    }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ActionData {
    pub r#type: String,
    pub usr: String,
    pub x: u32,
    pub y: u32,
}

impl ActionData {
    pub fn to_msg(self) -> Message {
        Message::Action(self)
    }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct JoinData {
    pub r#type: String,
}

impl JoinData {
    pub fn to_msg(self) -> Message {
        Message::Join(self)
    }
}
