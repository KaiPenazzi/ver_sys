use druid::{
    widget::{Button, Flex, Label, TextBox},
    Widget, WidgetExt,
};

use crate::AppData;

pub async fn ui_builder() -> impl Widget<AppData> {
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

    root.add_child(
        Button::new("create").on_click(|_ctx, data: &mut AppData, _env| {
            let mut manager = data.manager.lock().unwrap();
            manager.start_game(
                data.input_port.parse::<i32>().unwrap(),
                data.input_x.parse::<u32>().unwrap(),
                data.input_y.parse::<u32>().unwrap(),
                data.input_k.parse::<u32>().unwrap(),
            )
        }),
    );

    root.add_child(
        Button::new("test").on_click(|_ctx, data: &mut AppData, _env| {
            let mut manager = data.manager.lock().unwrap();
            manager.action(1, 2);
        }),
    );

    for y in 0..3 {
        let mut row = Flex::row();
        for x in 0..3 {
            let mut column = Flex::column();
            let x2 = x.clone();
            let y2 = y.clone();
            column.add_child(Button::new("None".to_string()).on_click(
                move |_ctx, data: &mut AppData, _env| {
                    let mut manager = data.manager.lock().unwrap();
                    manager.action(x2.clone(), y2.clone())
                },
            ));
            row.add_child(column)
        }
        root.add_child(row)
    }

    root
}
