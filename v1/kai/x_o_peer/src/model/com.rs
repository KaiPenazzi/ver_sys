use std::net::{IpAddr, SocketAddr};

use druid::{Data, Lens};

use super::messages::{JoinData, LeaveData, Message};

#[derive(Debug, Clone, Data, Lens)]
pub struct Peer {
    pub url: SocketAddr,
    pub usr: String,
}
impl Peer {
    pub fn new(ip: IpAddr, port: u16, usr: String) -> Self {
        Self {
            url: SocketAddr::new(ip, port),
            usr: usr.clone(),
        }
    }

    pub fn from_url(url: &SocketAddr) -> Self {
        let url = url.clone();
        Self {
            url,
            usr: "".to_string(),
        }
    }

    pub fn to_url(&self) -> String {
        self.url.to_string()
    }

    pub fn to_join(&self) -> JoinData {
        JoinData {
            r#type: "join".to_string(),
            usr: self.usr.clone(),
            ip: self.url.ip().to_string(),
            port: self.url.port(),
        }
    }

    pub fn to_leave(&self) -> LeaveData {
        LeaveData {
            r#type: "leave".to_string(),
            usr: self.usr.clone(),
            ip: self.url.ip().to_string(),
            port: self.url.port(),
        }
    }
}

impl PartialEq for Peer {
    fn eq(&self, other: &Self) -> bool {
        self.url == other.url && self.usr == other.usr
    }
}

pub struct SendMsg {
    pub msg: Message,
    pub send_to: Vec<Peer>,
}

pub struct RecvMsg {
    pub msg: Message,
    pub from: IpAddr,
}
