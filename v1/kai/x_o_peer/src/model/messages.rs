use serde::{Deserialize, Serialize};

use crate::game::scores::Score;

#[derive(Debug, Deserialize, Serialize)]
pub enum Message {
    Init(InitData),
    Action(ActionData),
    Join(JoinData),
    Leave(LeaveData),
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
    pub usr: String,
    pub ip: String,
    pub port: u16,
}
impl JoinData {
    pub fn to_msg(self) -> Message {
        Message::Join(self)
    }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct LeaveData {
    pub r#type: String,
    pub usr: String,
}

impl LeaveData {
    pub fn to_msg(self) -> Message {
        Message::Leave(self)
    }
}
