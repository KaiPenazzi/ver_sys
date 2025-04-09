use serde::{Deserialize, Serialize};
use serde_json::json;

use crate::game::scores::Score;

// #[derive(Debug, Deserialize, Serialize)]
// pub struct Message {
//     pub r#type: String,
//     pub data: serde_json::Value,
// }
// impl Message {
//     pub fn action(action: ActionData) -> Self {
//         Self {
//             r#type: "action".to_string(),
//             data: json!(action),
//         }
//     }
//
//     pub fn init(init: InitData) -> Self {
//         Self {
//             r#type: "init".to_string(),
//             data: json!(init),
//         }
//     }
//
//     pub fn join() -> Self {
//         Self {
//             r#type: "join".to_string(),
//             data: json!(""),
//         }
//     }
// }

pub struct Message {
    pub init: Option<InitData>,
    pub action: Option<ActionData>,
    pub join: Option<JoinData>,
}

impl Message {
    pub fn get_msg(self) -> serde_json::Value {
        match self.init {
            Some(init) => return json!(init),
            None => {}
        }
        match self.action {
            Some(init) => return json!(init),
            None => {}
        }
        match self.join {
            Some(init) => return json!(init),
            None => {}
        }
        json!({})
    }

    pub fn from_json(input: [u8])
}

#[derive(Debug, Deserialize, Serialize)]
pub struct InitData {
    pub r#type: String,
    pub field: Vec<Vec<String>>,
    pub score: Vec<Score>,
    pub k: u32,
}

impl InitData {
    pub fn to_msg(self) -> Message {
        Message {
            init: self,
            action: None,
            join: None,
        }
    }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ActionData {
    pub r#type: String,
    pub usr: String,
    pub x: u32,
    pub y: u32,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct JoinData {
    pub r#type: String,
}
