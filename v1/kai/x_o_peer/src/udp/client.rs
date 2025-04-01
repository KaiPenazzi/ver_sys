use std::{
    net::IpAddr,
    sync::{mpsc::Sender, Arc, Mutex},
};

use druid::{im::Vector, Data};

use crate::{
    model::{
        com::{Peer, SendMsg},
        messages::{ActionData, InitData, Message},
    },
    PORT_A,
};

#[derive(Data, Clone)]
pub struct Client {
    peers: Vector<Peer>,
    msg_q: Arc<Mutex<Sender<SendMsg>>>,
}

impl Client {
    pub fn new(tx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        let mut client = Self {
            peers: Vector::new(),
            msg_q: tx,
        };

        client.peers.push_back(Peer {
            ip: "127.0.0.1".parse().unwrap(),
            port: PORT_A,
        });

        client
    }

    fn send(&self, data: Message, to: Option<IpAddr>) {
        let mut send_to: Vec<Peer>;

        match to {
            Some(ip) => {
                println!("{}", ip);
                send_to = vec![self
                    .peers
                    .iter()
                    .find(|peer| peer.ip == ip)
                    .unwrap()
                    .clone()]
            }
            None => {
                send_to = Vec::new();
                for peer in self.peers.clone() {
                    send_to.push(peer);
                }
            }
        }

        let tx = self.msg_q.lock().unwrap();
        tx.send(SendMsg {
            msg: data,
            send_to: send_to,
        })
        .unwrap();
    }

    pub fn send_action(&self, data: ActionData) {
        self.send(Message::action(data), None);
    }

    pub fn send_init(&self, data: InitData, to: Option<IpAddr>) {
        self.send(Message::init(data), to);
    }

    pub fn send_join(&self) {
        let peer = self.peers.get(0).unwrap();
        self.send(Message::join(), Some(peer.ip.clone()));
    }
}
