use controlls::controlls;
use druid::{
    theme::{BUTTON_BORDER_RADIUS, BUTTON_DARK, WINDOW_BACKGROUND_COLOR},
    widget::{Button, Flex, Label, List, TextBox},
    Color, Env, LensExt, Widget, WidgetExt,
};
use ui_cell::UiCell;

use crate::{
    game::{
        field::{Cell, GameField, Row},
        scores::{GameScores, Score},
        Game,
    },
    manager::Manager,
    model::com::Peer,
    udp::client::Client,
    AppData,
};

mod controlls;
mod ui_cell;

pub fn ui_builder() -> impl Widget<AppData> {
    let mut root = Flex::column();

    root.add_child(controlls());

    let list_cols = List::new(|| {
        let list_rows = List::new(|| UiCell::new().on_click(Cell::on_click)).lens(Row::cells);
        Flex::column().with_child(list_rows)
    })
    .horizontal()
    .lens(AppData::manager.then(Manager::game.then(Game::field.then(GameField::cols))));
    root.add_child(Flex::column().with_child(list_cols));

    let list_scores = List::new(|| {
        let mut row = Flex::row();
        row.add_child(Label::new(|data: &Score, _env: &Env| data.usr.clone()));
        row.add_child(Label::new(|data: &Score, _env: &Env| {
            data.points.to_string()
        }));

        row
    })
    .lens(AppData::manager.then(Manager::game.then(Game::scores.then(GameScores::scores))));
    root.add_child(Flex::column().with_child(list_scores).padding((20.0, 20.)));

    root.add_child(udp_client());

    root
}

fn udp_client() -> impl Widget<AppData> {
    let root = Flex::column();

    let peers = List::new(|| {
        let mut row = Flex::row();
        row.add_child(Label::new(|peer: &Peer, _env: &Env| peer.usr.clone()));

        row.add_child(Label::new(|peer: &Peer, _env: &Env| peer.to_url()));

        row
    })
    .lens(AppData::manager.then(Manager::msq_client.then(Client::peers)));

    root.with_child(peers)
}

pub fn my_theme(env: &mut Env, _data: &AppData) {
    env.set(
        WINDOW_BACKGROUND_COLOR,
        Color::from_hex_str("#00001a").unwrap(),
    );
    env.set(BUTTON_DARK, Color::from_hex_str("#4d004d").unwrap());
    env.set(BUTTON_BORDER_RADIUS, 2.);
}
