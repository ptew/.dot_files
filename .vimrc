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

" include plugins
" call pathogen#infect()

syntax on " syntax highlighting on
filetype plugin indent on

" shortcut mappings
nnoremap <F1> :tabprev<CR>
inoremap <F1> <ESC>:tabprev<CR>
nnoremap <F2> :tabnext<CR>
inoremap <F2> <ESC>:tabnext<CR>
" nnoremap <F3> :NERDTreeToggle<CR>
 "inoremap <F3> <ESC>:NERDTreeToggle<CR>
nnoremap <F4> <C-w>w
inoremap <F4> <ESC><C-w>w

nnoremap <F5> :setlocal spell spelllang=en_us<CR>
inoremap <F5> <ESC>:setlocal spell spellland=en_us<CR>i
nnoremap <F6> :setlocal nospell<CR>
inoremap <F6> <ESC>:setlocal nospell<CR>i

" remap ctrl+s to save
inoremap <C-s> <ESC>:w<CR>
nnoremap <C-s> :w<CR>

" remap ctrl+q to quit
inoremap <C-q> <ESC>:q!<CR>
nnoremap <C-q> :q!<CR>

" remap ctrl+Q to quit all
inoremap <C-Q> <ESC>:qa!<CR>
nnoremap <C-Q> :qa!<CR>

" if vim starts w/ no args, then NERDTree pops up
" autocmd vimenter * if !argc() | NERDTree | endif
" if vim only has NERDTree open, then it closes
autocmd bufenter * if (winnr("$") == 1 && exists("b:NERDTreeType") && b:NERDTreeType == "primary") | q | endif

" allow windows like copy paste
source $VIMRUNTIME/mswin.vim

if has("autocmd")
	 filetype plugin on
	 filetype indent on
endif " has ("autocmd")
let b:verilog_indent_modules = 1
