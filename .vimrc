colorscheme darkblue

" make vim normal
set whichwrap+=<,>,h,l,[,] " wrap scrolling around lines
set backspace=eol,start,indent " backspace deletes newlines
set statusline=%F%m%r%h%w\ %=[POS=%01l,%01v]\ [LEN=%L]

" searching
set ignorecase " ignore case when searching
set incsearch " show search matches while you type

" indenting
set expandtab " tab becomes space
set laststatus=2 " status line
set shiftwidth=2 " two-space tabs
set tabstop=2 " two-space tabs
set autoindent " auto-indent on cr
set smartindent " indenting is smart!
set copyindent " copy previous indent on autoindenting
set scrolloff=10 " three lines of offset while scrolling

" numbers
set nu " numbered lines
set nuw=1 " minimum width of line numbers

syntax on " syntax highlighting on
filetype plugin indent on

let b:verilog_indent_modules = 1
