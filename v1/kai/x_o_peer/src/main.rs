mod coroutine;
pub mod game;
pub mod manager;
pub mod model;
pub mod udp;
mod ui;

use std::{
    sync::{Arc, Mutex},
    thread,
};

use coroutine::run_server;
use druid::{AppLauncher, Data, Lens, WindowDesc};
use game::Game;
use manager::Manager;
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
    let manager = Manager::new("kai".to_string());
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

    launcher
        .log_to_console()
        .launch(app_data)
        .expect("launch failed");
}
