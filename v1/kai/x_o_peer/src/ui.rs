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

pub fn controlls() -> impl Widget<AppData> {
    let mut root = Flex::column();

    {
        let mut row = Flex::row();
        row.add_child(Label::new("x-size:").padding((10., 0.)));
        row.add_child(
            TextBox::new()
                .with_placeholder("x-size")
                .lens(AppData::manager.then(Manager::x_size))
                .fix_width(165.0),
        );
        root.add_child(row.padding((0., 2.)));
    }

    {
        let mut row = Flex::row();
        row.add_child(Label::new("y-size:").padding((10.0, 0.)));
        row.add_child(
            TextBox::new()
                .with_placeholder("y-size")
                .lens(AppData::manager.then(Manager::y_size))
                .fix_width(165.0),
        );
        root.add_child(row.padding((0., 2.)));
    }

    {
        let mut row = Flex::row();
        row.add_child(Label::new("k-size:").padding((10.0, 0.)));
        row.add_child(
            TextBox::new()
                .with_placeholder("k")
                .lens(AppData::manager.then(Manager::k_size))
                .fix_width(165.0),
        );
        root.add_child(row.padding((0., 2.)));
    }

    {
        let mut row = Flex::row();
        row.add_child(
            Button::new("New Game")
                .on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.new_game())
                .fix_size(110., 50.)
                .padding((10., 0.)),
        );
        row.add_child(
            Button::new("Join")
                .on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.join())
                .fix_size(110., 50.)
                .padding((10., 0.)),
        );
        root.add_child(row.padding((0., 5.)))
    }

    root.padding((20.0, 20.0))
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
