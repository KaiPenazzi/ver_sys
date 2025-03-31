use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
pub struct Message {
    pub r#type: String,
    pub data: serde_json::Value,
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

//#[derive(Debug, Deserialize, Serialize)]
//pub struct SendAction {
//    r#type: String,
//    data: ActionData,
//}

pub struct Peer {
    ip: String,
    port: u32,
}
impl Peer {
    pub fn new(id: String, port: u32) -> Self {
        Self { ip: id, port: port }
    }
    pub fn to_url(self) -> String {
        return format!("{}:{}", self.ip, self.port.to_string());
    }
}

pub struct SendMsg {
    pub msg: Message,
    pub send_to: Vec<Peer>,
}
