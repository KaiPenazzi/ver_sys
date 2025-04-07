use std::{
    net::IpAddr,
    sync::{mpsc::Sender, Arc, Mutex},
};

use druid::{im::Vector, Data, Lens};

use crate::{
    model::{
        com::{Peer, SendMsg},
        messages::{ActionData, InitData, Message},
    },
    PORT_A,
};

#[derive(Data, Clone, Lens)]
pub struct Client {
    pub peers: Vector<Peer>,
    pub new_url: String,
    msg_q: Arc<Mutex<Sender<SendMsg>>>,
}

impl Client {
    pub fn new(tx: Arc<Mutex<Sender<SendMsg>>>, urls: Vec<String>) -> Self {
        let mut client = Self {
            peers: Vector::new(),
            msg_q: tx,
            new_url: "".to_string(),
        };

        for url in urls {
            client.peers.push_back(Peer::from_url(&url).unwrap())
        }

        client
    }

    pub fn add(&mut self) {
        match Peer::from_url(&self.new_url) {
            Some(peer) => {
                self.peers.push_back(peer);
                self.new_url = "".to_string()
            }
            None => {}
        }
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
        let peer = self.peers.get(0);

        match peer {
            Some(peer) => self.send(Message::join(), Some(peer.ip.clone())),
            None => println!("no peer is known"),
        }
    }
}
