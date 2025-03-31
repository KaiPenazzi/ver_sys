mod coroutine;
pub mod eve;
pub mod game;
pub mod manager;
pub mod model;
pub mod udp;
mod ui;

use std::{
    sync::{mpsc, Arc, Mutex},
    thread,
};

const PORT: u32 = 1234;

use coroutine::{run_cleint, run_server};
use druid::{AppLauncher, Data, Lens, WindowDesc};
use eve::Delegate;
use manager::Manager;
use model::messages::SendMsg;
use ui::ui_builder;

#[derive(Clone, Data, Lens)]
struct AppData {
    manager: Manager,
    input_port: String,
    input_x: String,
    input_y: String,
    input_k: String,
}

#[tokio::main]
async fn main() {
    let (tx, mut rx) = mpsc::channel::<SendMsg>();
    let tx_arc = Arc::new(Mutex::new(tx));
    let manager = Manager::new("kai".to_string(), tx_arc);
    let app_data = AppData {
        manager: manager,
        input_port: "1234".to_string(),
        input_x: "3".to_string(),
        input_y: "3".to_string(),
        input_k: "3".to_string(),
    };

    let main_window = WindowDesc::new(ui_builder());
    let launcher = AppLauncher::with_window(main_window);

    let event_sink = launcher.get_external_handle();
    tokio::spawn(run_server(event_sink));

    tokio::spawn(run_cleint(rx));

    launcher
        .delegate(Delegate)
        .log_to_console()
        .launch(app_data)
        .expect("launch failed");
}
