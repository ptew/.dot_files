set nocompatible              " be iMproved, required
filetype off                  " required

" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()

" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'

" The following are examples of different formats supported.
" Keep Plugin commands between vundle#begin/end.
" plugin on GitHub repo
Plugin 'tpope/vim-fugitive'
Plugin 'flazz/vim-colorschemes'
Plugin 'vim-airline/vim-airline'
Plugin 'vim-airline/vim-airline-themes'
Plugin 'ctrlpvim/ctrlp.vim'
Plugin 'leafgarland/typescript-vim'
Plugin 'morhetz/gruvbox'
Plugin 'vim-scripts/CycleColor'

" All of your Plugins must be added before the following line
call vundle#end()            " required
filetype plugin indent on    " required

syntax enable
set background=dark
colorscheme solarized

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

" Ctrlp
let g:ctrlp_map = '<c-p>'
let g:ctrlp_cmd = 'CtrlP'

" Airline
let g:airline_theme='luna'

