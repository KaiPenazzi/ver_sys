use std::{
    net::{IpAddr, SocketAddr},
    str::FromStr,
};

use serde::{Deserialize, Serialize};

use crate::game::scores::Score;

use super::com::Peer;

pub trait ToPeer {
    fn to_peer(&self) -> Peer;
}

macro_rules! impl_to_peer {
    ($t:ty) => {
        impl ToPeer for $t {
            fn to_peer(&self) -> Peer {
                Peer {
                    url: SocketAddr::new(IpAddr::from_str(&self.ip).unwrap(), self.port),
                    usr: self.usr.clone(),
                }
            }
        }
    };
}

#[derive(Debug, Deserialize, Serialize)]
pub enum Message {
    Init(InitData),
    Action(ActionData),
    Join(JoinData),
    Leave(LeaveData),
    NewPlayer(NewPlayerData),
}

#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct PeerData {
    pub usr: String,
    pub ip: String,
    pub port: u16,
}

#[derive(Clone, Debug, Deserialize, Serialize)]
pub struct InitData {
    pub r#type: String,
    pub field: Vec<Vec<String>>,
    pub score: Vec<Score>,
    pub k: u32,
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
    pub usr: String,
    pub ip: String,
    pub port: u16,
}

impl_to_peer!(JoinData);

#[derive(Debug, Deserialize, Serialize)]
pub struct LeaveData {
    pub r#type: String,
    pub usr: String,
    pub ip: String,
    pub port: u16,
}

impl_to_peer!(LeaveData);

#[derive(Debug, Deserialize, Serialize)]
pub struct NewPlayerData {
    pub r#type: String,
    pub usr: String,
    pub ip: String,
    pub port: u16,
}

impl_to_peer!(NewPlayerData);
