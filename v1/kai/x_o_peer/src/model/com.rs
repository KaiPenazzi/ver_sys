use std::{
    net::{IpAddr, SocketAddr},
    str::FromStr,
};

use druid::{Data, Lens};

use super::messages::Message;

#[derive(Debug, Clone, Data, Lens)]
pub struct Peer {
    pub url: SocketAddr,
    pub usr: String,
}
impl Peer {
    pub fn new(ip: IpAddr, port: u16, usr: String) -> Self {
        Self {
            url: SocketAddr::new(ip, port),
            usr: usr,
        }
    }
    pub fn to_url(&self) -> String {
        self.url.to_string()
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
