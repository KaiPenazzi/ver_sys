use druid::Target;
use serde_json::from_value;
use tokio::net::UdpSocket;

use crate::{
    eve::UDP_MSG_RECV,
    model::{
        com::RecvMsg,
        messages::{ActionData, InitData, JoinData, Message},
    },
};

pub async fn run_server(event_sink: druid::ExtEventSink, port: String) -> std::io::Result<()> {
    let socket = UdpSocket::bind(format!("0.0.0.0:{}", port)).await?;
    println!("server is running");

    let mut buf: [u8; 1024] = [0; 1024];
    loop {
        let (len, addr) = socket.recv_from(&mut buf).await?;
        let val: serde_json::Value = serde_json::from_slice(&buf[..len]).unwrap(); // JSON → serde_json::Value
        let msg_type = val.get("type").unwrap().as_str().unwrap(); // type-Feld extrahieren

        println!("{:?}", msg_type);

        let msg = match msg_type {
            "action" => {
                let act = from_value::<ActionData>(val).unwrap();
                Message::Action(act)
            }
            "init" => {
                let init = from_value::<InitData>(val).unwrap();
                Message::Init(init)
            }
            "join" => {
                let join = from_value::<JoinData>(val).unwrap();
                Message::Join(join)
            }
            _ => {
                println!("not supported msg type");
                Message::Join(JoinData {
                    r#type: "join".to_string(),
                })
            }
        };

        // println!("{:?}", &val);

        let _ = event_sink.submit_command(
            UDP_MSG_RECV,
            RecvMsg {
                msg: msg,
                from: addr.ip(),
            },
            Target::Global,
        );
    }
}
