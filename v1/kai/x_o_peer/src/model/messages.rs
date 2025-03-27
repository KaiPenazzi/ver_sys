use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct Message {
    pub r#type: String,
    pub data: serde_json::Value,
}

#[derive(Debug, Deserialize)]
pub struct InitData {
    pub field: Vec<Vec<String>>,
    pub k: u32,
}

#[derive(Debug, Deserialize)]
pub struct ActionData {
    pub usr: String,
    pub x: u32,
    pub y: u32,
}
