use druid::{
    widget::{Button, Flex, Label, List, TextBox},
    LensExt, Widget, WidgetExt,
};

use crate::{
    game::{
        field::{Cell, GameField, Row},
        Game,
    },
    manager::Manager,
    AppData,
};

pub fn ui_builder() -> impl Widget<AppData> {
    let mut root = Flex::column();

    {
        let mut row = Flex::row();
        row.add_child(Label::new("port: "));
        row.add_child(
            TextBox::new()
                .with_placeholder("port")
                .lens(AppData::input_port)
                .fix_width(200.0),
        );
        root.add_child(row);
    }

    {
        let mut row = Flex::row();
        row.add_child(Label::new("x-size: "));
        row.add_child(
            TextBox::new()
                .with_placeholder("x-size")
                .lens(AppData::input_x)
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
                .lens(AppData::input_y)
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
                .lens(AppData::input_k)
                .fix_width(200.0),
        );
        root.add_child(row);
    }

    let list_cols = List::new(|| {
        let list_rows =
            List::new(|| TextBox::new().lens(Cell::text).on_click(Cell::on_click)).lens(Row::cells);
        Flex::column().with_child(list_rows)
    })
    .horizontal()
    .lens(AppData::manager.then(Manager::game.then(Game::field.then(GameField::cols))));

    root.with_child(list_cols)
}
