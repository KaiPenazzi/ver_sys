use std::{
    net::{IpAddr, SocketAddr},
    str::FromStr,
};

use druid::{Data, Lens};

use super::messages::Message;

#[derive(Debug, Clone, Data, Lens)]
pub struct Peer {
    pub ip: IpAddr,
    pub port: u32,
}
impl Peer {
    pub fn new(id: IpAddr, port: u32) -> Self {
        Self { ip: id, port: port }
    }
    pub fn to_url(&self) -> String {
        return format!("{}:{}", self.ip, self.port.to_string());
    }
    pub fn from_url(url: &str) -> Option<Self> {
        match SocketAddr::from_str(url) {
            Ok(addr) => {
                return Some(Self {
                    ip: addr.ip(),
                    port: addr.port().try_into().unwrap(),
                });
            }
            Err(_) => println!("could not parse the url: {}", url),
        }

        None
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
