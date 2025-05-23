use druid::{im::Vector, Color, Data, Env, EventCtx, Lens};

use crate::{eve::FIELD_CLICKED, model::messages::ActionData};

#[derive(Clone, Data, Lens)]
pub struct GameField {
    pub cols: Vector<Row>,
    pub x: usize,
    pub y: usize,
    pub k: u32,
}

impl GameField {
    pub fn init(x: u32, y: u32, k: u32) -> Self {
        let mut field = Self {
            x: x as usize,
            y: y as usize,
            k: k,
            cols: Vector::new(),
        };

        for x_t in 0..x {
            field.cols.push_back(Row::new(x_t, y));
        }

        field
    }

    pub fn init_with_fields(field: Vec<Vec<String>>, k: u32) -> Self {
        if field.len() == 0 || field[0].len() == 0 {
            return Self::init(0, 0, k);
        }

        let mut grid = Self {
            x: field.len(),
            y: field[0].len(),
            k: k,
            cols: Vector::new(),
        };

        for (x_t, vec) in field.iter().enumerate() {
            grid.cols
                .push_back(Row::from_vec(vec, x_t.try_into().unwrap()))
        }

        grid
    }

    pub fn set(&mut self, action: &ActionData) {
        let x = action.x as usize;
        let y = action.y as usize;

        if let Some(row) = self.cols.get_mut(x) {
            if let Some(cell) = row.cells.get_mut(y) {
                if cell.text == "none" {
                    cell.set(&action.usr);
                }
            }
        }
    }

    pub fn get(&self, x: usize, y: usize) -> Option<&Cell> {
        if let Some(row) = self.cols.get(x) {
            if let Some(cell) = row.cells.get(y) {
                return Some(cell);
            }
        }

        println!("got none");
        None
    }

    pub fn to_vec(&self) -> Vec<Vec<String>> {
        let mut col = vec![];

        for x in self.cols.clone() {
            let mut row = vec![];
            for y in x.cells {
                row.push(y.text)
            }
            col.push(row)
        }

        col
    }

    pub fn reset(&mut self, x: u32, y: u32) {
        let x = x as usize;
        let y = y as usize;

        if let Some(row) = self.cols.get_mut(x) {
            if let Some(cell) = row.cells.get_mut(y) {
                cell.reset()
            }
        }
    }

    pub fn check(&mut self) -> Option<String> {
        match self.check_col() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_row() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_diagonal_lr() {
            Some(name) => return Some(name),
            None => (),
        };
        match self.check_diagonal_rl() {
            Some(name) => return Some(name),
            None => (),
        };

        None
    }

    fn check_col(&mut self) -> Option<String> {
        for col in self.cols.clone() {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<&Cell> = vec![];

            for cell in &col.cells {
                if cell.text != "none" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_row(&mut self) -> Option<String> {
        for y in 0..self.y {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];

            for x in 0..self.x {
                let cell = self
                    .get(x.try_into().unwrap(), y.try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "none" && cell.text == start_name {
                    count += 1;
                    cells.push(cell)
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_diagonal_lr(&mut self) -> Option<String> {
        for x in 0..self.x {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for y in (0..self.y).filter(|y| x + y < self.x) {
                let cell = self
                    .get((x + y).try_into().unwrap(), y.try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "none" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        for y in 0..self.y {
            let mut count = 0;
            let mut start_name = "".to_string();
            let mut cells: Vec<Cell> = vec![];
            for x in (0..self.x).filter(|x| x + y < self.y) {
                let cell = self
                    .get(x.try_into().unwrap(), (x + y).try_into().unwrap())
                    .unwrap()
                    .clone();
                if cell.text != "none" && cell.text == start_name {
                    count += 1;
                    cells.push(cell);
                } else {
                    start_name = cell.text.clone();
                    count = 1;
                    cells.clear();
                    cells.push(cell);
                }

                if count == self.k {
                    for cell in cells {
                        self.reset(cell.x, cell.y)
                    }
                    return Some(start_name);
                }
            }
        }

        None
    }

    fn check_diagonal_rl(&mut self) -> Option<String> {
        for x in (0..self.x).rev() {
            let mut cells = vec![Cell::new(0, 0)];
            for y in 0..self.y {
                if x >= y {
                    let cell = self.get(x - y, y).unwrap().clone();

                    if cell.text != "none" && cells[0].text == cell.text {
                        cells.push(cell);
                    } else {
                        cells.clear();
                        cells.push(cell);
                    }

                    if cells.len() == self.k as usize {
                        for cell_tmp in &cells {
                            self.reset(cell_tmp.x, cell_tmp.y)
                        }

                        return Some(cells[0].text.clone());
                    }
                }
            }
        }

        for y in 1..self.y {
            let mut cells = vec![Cell::new(0, 0)];
            for i in 0..self.x {
                if y + i < self.y {
                    let cell = self.get(self.x - 1 - i, y + i).unwrap().clone();

                    if cell.text != "none" && cells[0].text == cell.text {
                        cells.push(cell);
                    } else {
                        cells.clear();
                        cells.push(cell);
                    }

                    if cells.len() == self.k as usize {
                        for cell_tmp in &cells {
                            self.reset(cell_tmp.x, cell_tmp.y)
                        }

                        return Some(cells[0].text.clone());
                    }
                }
            }
        }

        None
    }
}

#[derive(Clone, Data, Lens)]
pub struct Row {
    pub cells: Vector<Cell>,
}
impl Row {
    fn new(x: u32, y: u32) -> Self {
        let mut row = Self {
            cells: Vector::new(),
        };

        for y_t in 0..y {
            row.cells.push_back(Cell::new(x, y_t));
        }

        row
    }

    fn from_vec(vec: &Vec<String>, x: u32) -> Self {
        let mut row = Self {
            cells: Vector::new(),
        };

        for (y_t, text) in vec.iter().enumerate() {
            row.cells
                .push_back(Cell::new_text(text.to_string(), x, y_t.try_into().unwrap()))
        }

        row
    }
}

#[derive(Clone, Data, Lens)]
pub struct Cell {
    pub text: String,
    pub x: u32,
    pub y: u32,
    pub color: Color,
}
impl Cell {
    fn new(x: u32, y: u32) -> Self {
        Self {
            text: "none".to_string(),
            x: x,
            y: y,
            color: Color::BLACK,
        }
    }

    fn new_text(text: String, x: u32, y: u32) -> Self {
        Self {
            text: text,
            x: x,
            y: y,
            color: Color::BLACK,
        }
    }

    pub fn set(&mut self, name: &String) {
        self.text = name.clone();
    }

    pub fn on_click(_ctx: &mut EventCtx, data: &mut Cell, _env: &Env) {
        _ctx.submit_command(FIELD_CLICKED.with(data.clone()));
    }

    pub fn reset(&mut self) {
        self.text = "none".to_string()
    }
}

#[cfg(test)]
mod test_field {
    use crate::model::messages::ActionData;

    use super::GameField;

    #[test]
    fn test_col() {
        let mut field = GameField::init(3, 3, 2);
        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 1,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_row() {
        let mut field = GameField::init(3, 3, 2);
        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 0,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_diag_lr() {
        let mut field = GameField::init(3, 3, 2);
        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 0,
        });

        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 1,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_diag_rl() {
        let mut field = GameField::init(4, 4, 3);
        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 2,
            y: 1,
        });

        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 1,
            y: 1,
        });

        field.set(&ActionData {
            usr: "tim".to_string(),
            x: 0,
            y: 2,
        });

        assert_eq!(field.check(), Some("tim".to_string()))
    }

    #[test]
    fn test_to_vec() {
        let mut field = GameField::init(2, 4, 3);
        field.set(&ActionData {
            usr: "test".to_string(),
            x: 1,
            y: 2,
        });
        let expected = vec![
            vec!["none", "none", "none", "none"],
            vec!["none", "none", "test", "none"],
        ];

        assert_eq!(field.to_vec(), expected)
    }

    #[test]
    fn get_set() {
        let mut field = GameField::init(2, 4, 3);
        field.set(&ActionData {
            usr: "test".to_string(),
            x: 1,
            y: 3,
        });

        let cell = field.get(1, 3).unwrap();

        assert_eq!(cell.x, 1);
        assert_eq!(cell.y, 3);
        assert_eq!(cell.text, "test");
    }
}
