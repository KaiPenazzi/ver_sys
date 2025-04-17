use std::sync::{mpsc::Sender, Arc, Mutex};

use druid::{im::Vector, Data, Lens};

use crate::model::{
    com::{Peer, SendMsg},
    messages::{ActionData, InitData, JoinData, Message},
};

#[derive(Data, Clone, Lens)]
pub struct Client {
    pub peers: Vector<Peer>,
    msg_q: Arc<Mutex<Sender<SendMsg>>>,
}

impl Client {
    pub fn new(tx: Arc<Mutex<Sender<SendMsg>>>) -> Self {
        let client = Self {
            peers: Vector::new(),
            msg_q: tx,
        };

        client
    }

    fn send(&self, data: Message, to: Option<&Peer>) {
        let mut send_to: Vec<Peer>;

        match to {
            Some(peer) => send_to = vec![peer.clone()],
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
        self.send(Message::Action(data), None);
    }

    pub fn send_init(&self, data: InitData, to: Option<&Peer>) {
        self.send(Message::Init(data), to);
    }

    pub fn send_join(&self) {
        let peer = self.peers.get(0);

        match peer {
            Some(peer) => self.send(
                Message::Join(JoinData {
                    r#type: "join".to_string(),
                    usr: peer.usr.clone(),
                    ip: peer.url.ip().to_string(),
                    port: peer.url.port(),
                }),
                Some(peer),
            ),
            None => println!("no peer is known"),
        }
    }

    pub fn add(&mut self, peer: Peer) {
        self.peers.push_back(peer)
    }

    pub fn leave(&mut self, peer: &Peer) {
        self.peers.retain(|in_peer: &Peer| in_peer != peer);
    }
}
