// Mobile Menu
var hamburger = document.getElementById('hamburger');
var mobileMenu = document.getElementById('mobileMenu');
if (hamburger && mobileMenu) {
    hamburger.addEventListener('click', function() {
        mobileMenu.classList.toggle('active');
    });
    mobileMenu.querySelectorAll('a').forEach(function(link) {
        link.addEventListener('click', function() {
            mobileMenu.classList.remove('active');
        });
    });
}

// Dropdown
function toggleDropdown(e) {
    e.stopPropagation();
    var menu = e.currentTarget.nextElementSibling;
    if (menu) menu.classList.toggle('show');
}

document.addEventListener('click', function(e) {
    var dropdown = document.querySelector('.dropdown');
    if (dropdown && !dropdown.contains(e.target)) {
        var menu = dropdown.querySelector('.dropdown-menu');
        if (menu) menu.classList.remove('show');
    }
});

var toggle = document.querySelector('.dropdown-toggle');
if (toggle) {
    toggle.addEventListener('click', function(e) {
        e.stopPropagation();
        var menu = this.nextElementSibling;
        if (menu) menu.classList.toggle('show');
    });
}

// Alert auto-dismiss
setTimeout(function() {
    document.querySelectorAll('.alert').forEach(function(el) {
        el.style.opacity = '0';
        setTimeout(function() { if (el.parentNode) el.parentNode.removeChild(el); }, 300);
    });
}, 5000);
