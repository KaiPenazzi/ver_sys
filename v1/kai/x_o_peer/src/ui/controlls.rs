use druid::{
    widget::{Button, Flex, Label, TextBox},
    LensExt, Widget, WidgetExt,
};

use crate::{manager::Manager, AppData};

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
            TextBox::new()
                .with_placeholder("friend url")
                .lens(AppData::manager.then(Manager::friend))
                .fix_width(165.0),
        );

        row.add_child(
            Button::new("Join")
                .on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.join())
                .fix_size(110., 50.)
                .padding((10., 0.)),
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
            Button::new("Leave")
                .on_click(|_ev, appdata: &mut AppData, _env| appdata.manager.leave())
                .fix_size(110., 50.)
                .padding((10., 0.)),
        );
        root.add_child(row.padding((0., 5.)))
    }

    root.padding((20.0, 20.0))
}
