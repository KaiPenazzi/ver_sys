use std::{
    net::SocketAddr,
    sync::{mpsc::Sender, Arc, Mutex},
};

use druid::{im::Vector, Data, Lens};

use crate::model::{
    com::{Peer, SendMsg},
    messages::{
        ActionData, InitData, JoinData, Message, NewPlayerData, PeerData, PlayerData, ToPeer,
    },
};

#[derive(Data, Clone, Lens)]
pub struct Client {
    pub peers: Vector<Peer>,
    msg_q: Arc<Mutex<Sender<SendMsg>>>,
    pub own: Peer,
}

impl Client {
    pub fn new(tx: Arc<Mutex<Sender<SendMsg>>>, own: Peer) -> Self {
        let client = Self {
            peers: Vector::new(),
            msg_q: tx,
            own: own,
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
            send_to: send_to.clone(),
        })
        .unwrap();
    }

    pub fn send_action(&self, data: ActionData) {
        self.send(Message::Action(data), None);
    }

    pub fn send_init(&self, data: InitData, to: Option<&Peer>) {
        self.send(Message::Init(data), to);
    }

    pub fn send_join(&self, to: &SocketAddr) {
        self.send(Message::Join(self.own.to_join()), Some(&Peer::from_url(to)));
    }

    pub fn send_player(&self, to: &Peer) {
        let mut peer_data = PlayerData {
            r#type: "player".to_string(),
            players: vec![PeerData::from_peer(&self.own)],
        };

        for peer in &self.peers {
            peer_data.players.push(PeerData::from_peer(peer));
        }

        self.send(Message::Player(peer_data), Some(to));
    }

    pub fn send_new_player(&self, player: &JoinData) {
        self.send(Message::NewPlayer(player.to_new_player()), None);
    }

    pub fn add(&mut self, peer: Peer) {
        self.peers.push_back(peer)
    }

    pub fn leave(&mut self, peer: &Peer) {
        self.peers.retain(|in_peer: &Peer| in_peer != peer);
    }

    pub fn add_players(&mut self, player: PlayerData) {
        for peer in player.players {
            self.peers.push_back(peer.to_peer());
        }
    }
}
