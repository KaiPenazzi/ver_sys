use druid::{
    widget::{BackgroundBrush, Button, Flex, Label, List, TextBox},
    Color, Env, LensExt, Widget, WidgetExt,
};

use crate::{
    game::{
        field::{Cell, GameField, Row},
        scores::{GameScores, Score},
        Game,
    },
    manager::Manager,
    AppData,
};

pub fn ui_builder() -> impl Widget<AppData> {
    let mut root = Flex::column();

    //{
    //    let mut row = Flex::row();
    //    row.add_child(Label::new("port: "));
    //    row.add_child(
    //        TextBox::new()
    //            .with_placeholder("port")
    //            .lens(AppData::input_port)
    //            .fix_width(200.0),
    //    );
    //    root.add_child(row);
    //}

    {
        let mut row = Flex::row();
        row.add_child(Label::new("x-size: "));
        row.add_child(
            TextBox::new()
                .with_placeholder("x-size")
                .lens(AppData::manager.then(Manager::x_size))
                .fix_width(200.0),
        );
        root.add_child(row);
    }

    {
        let mut row = Flex::row();
        row.add_child(Label::new("y-size: "));
        row.add_child(
            TextBox::new()
                .with_placeholder("y-size")
                .lens(AppData::manager.then(Manager::y_size))
                .fix_width(200.0),
        );
        root.add_child(row);
    }

    {
        let mut row = Flex::row();
        row.add_child(Label::new("k-size: "));
        row.add_child(
            TextBox::new()
                .with_placeholder("k")
                .lens(AppData::manager.then(Manager::k_size))
                .fix_width(200.0),
        );
        root.add_child(row);
    }

    {
        let mut row = Flex::row();
        row.add_child(
            Button::new("Create new Game")
                .on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.new_game()),
        );
        row.add_child(
            Button::new("Join").on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.join()),
        );
        root.add_child(row)
    }

    let list_cols = List::new(|| {
        let list_rows = List::new(|| {
            Button::dynamic(|cell: &Cell, _env: &Env| cell.text.clone()).on_click(Cell::on_click)
        })
        .lens(Row::cells);
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
    root.add_child(Flex::column().with_child(list_scores));

    root
}
