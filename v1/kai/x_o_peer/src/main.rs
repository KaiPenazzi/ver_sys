pub mod coroutins;
pub mod eve;
pub mod game;
pub mod manager;
pub mod model;
mod pars;
pub mod udp;
mod ui;

use std::net::SocketAddr;
use std::str::FromStr;
use std::sync::{mpsc, Arc, Mutex};

pub const PORT_A: u32 = 1225;
pub const PORT: u32 = 1234;

use clap::Parser;
use coroutins::{client::run_cleint, server::run_server};
use druid::{AppLauncher, Data, Lens, WindowDesc};
use eve::Delegate;
use manager::Manager;
use model::com::Peer;
use model::com::SendMsg;
use pars::Args;
use udp::client::Client;
use ui::my_theme;
use ui::ui_builder;

#[derive(Clone, Data, Lens)]
struct AppData {
    manager: Manager,
}

#[tokio::main]
async fn main() {
    let args = Args::parse();

    println!("{:?}", args);

    let (tx, rx) = mpsc::channel::<SendMsg>();
    let tx_arc = Arc::new(Mutex::new(tx));
    let client = Client::new(
        tx_arc,
        Peer {
            usr: args.usr.clone(),
            url: SocketAddr::from_str(&args.url).unwrap(),
        },
    );

    let manager = Manager::new(args.usr, client);
    let app_data = AppData { manager: manager };

    let main_window = WindowDesc::new(ui_builder());
    let launcher = AppLauncher::with_window(main_window);

    let mut tasks = vec![];

    let event_sink = launcher.get_external_handle();
    tasks.push(tokio::spawn(run_server(
        event_sink,
        SocketAddr::from_str(&args.url).unwrap().port().to_string(),
    )));
    tasks.push(tokio::spawn(run_cleint(rx)));

    launcher
        .configure_env(my_theme)
        .delegate(Delegate)
        .log_to_console()
        .launch(app_data)
        .expect("launch failed");

    for task in tasks {
        task.abort();
    }
}
