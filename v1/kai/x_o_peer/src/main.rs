mod coroutine;
pub mod eve;
pub mod game;
pub mod manager;
pub mod model;
mod pars;
pub mod udp;
mod ui;

use std::sync::{mpsc, Arc, Mutex};

pub const PORT_A: u32 = 1225;
pub const PORT: u32 = 1234;

use clap::Parser;
use coroutine::{run_cleint, run_server};
use druid::{AppLauncher, Data, Lens, WindowDesc};
use eve::Delegate;
use manager::Manager;
use model::com::SendMsg;
use pars::Args;
use ui::ui_builder;

#[derive(Clone, Data, Lens)]
struct AppData {
    manager: Manager,
    input_x: String,
    input_y: String,
    input_k: String,
}

#[tokio::main]
async fn main() {
    let args = Args::parse();

    let (tx, rx) = mpsc::channel::<SendMsg>();
    let tx_arc = Arc::new(Mutex::new(tx));
    let manager = Manager::new(args.usr, args.urls, tx_arc);
    let app_data = AppData {
        manager: manager,
        input_x: "3".to_string(),
        input_y: "3".to_string(),
        input_k: "3".to_string(),
    };

    let main_window = WindowDesc::new(ui_builder());
    let launcher = AppLauncher::with_window(main_window);

    let mut tasks = vec![];

    let event_sink = launcher.get_external_handle();
    tasks.push(tokio::spawn(run_server(event_sink, args.port)));
    tasks.push(tokio::spawn(run_cleint(rx)));

    launcher
        .delegate(Delegate)
        .log_to_console()
        .launch(app_data)
        .expect("launch failed");

    for task in tasks {
        task.abort();
    }
}
