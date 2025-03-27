pub struct GameField {
    pub field: Vec<Vec<String>>,
}

impl GameField {
    pub fn new(field: Vec<Vec<String>>) -> Self {
        Self { field: field }
    }

    pub fn init(x: usize, y: usize) -> Self {
        Self {
            field: vec![vec!["None".to_string(); y]; x],
        }
    }

    pub fn set(&mut self, x: usize, y: usize, name: &str) {
        self.field[x][y] = name.to_string()
    }
}
