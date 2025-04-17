use std::{
    net::{IpAddr, SocketAddr},
    str::FromStr,
};

use druid::{Data, Lens};

use crate::game::scores::GameScores;

use super::messages::{JoinData, Message};

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

    pub fn from_join(join: &JoinData) -> Self {
        Self {
            url: SocketAddr::new(IpAddr::from_str(&join.ip).unwrap(), join.port.clone()),
            usr: join.usr.clone(),
        }
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
