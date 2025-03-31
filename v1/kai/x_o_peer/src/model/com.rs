use druid::{Data, Lens};

use super::messages::Message;

#[derive(Debug, Clone, Data, Lens)]
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
