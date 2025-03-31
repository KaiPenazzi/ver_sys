use serde::{Deserialize, Serialize};
use serde_json::json;

#[derive(Debug, Deserialize, Serialize)]
pub struct Message {
    pub r#type: String,
    pub data: serde_json::Value,
}
impl Message {
    pub fn action(action: ActionData) -> Self {
        Self {
            r#type: "action".to_string(),
            data: json!(action),
        }
    }

    pub fn init(init: InitData) -> Self {
        Self {
            r#type: "init".to_string(),
            data: json!(init),
        }
    }
}

#[derive(Debug, Deserialize, Serialize)]
pub struct InitData {
    pub field: Vec<Vec<String>>,
    pub k: u32,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ActionData {
    pub usr: String,
    pub x: u32,
    pub y: u32,
}
