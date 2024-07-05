
  document.addEventListener('DOMContentLoaded', function() {
    const currentUrl = window.location.pathname;

    const navLinks = [
      { id: 'home-link', href: '/' },
      { id: 'field-link', href: '/field' },
      { id: 'files-link', href: '/files' },
      { id: 'about-link', href: '/about' }
    ];

    function updateNavLinks() {
      navLinks.forEach(link => {
        const element = document.getElementById(link.id);
        if (currentUrl === link.href) {
          element.classList.remove('text-white');
          element.classList.add('text-secondary');
        } else {
          element.classList.remove('text-secondary');
          element.classList.add('text-white');
        }
      });
    }

    updateNavLinks();
    document.getElementById('user-login').textContent = server.user.login;
  });
  

  if(server.user.authentication == "USER") {
    document.getElementById('text-end-element').hidden = true;
    document.getElementById('profile-element').hidden = false;
    document.getElementById('button-logout').hidden = false;
  }
  else {
    document.getElementById('text-end-element').hidden = false;
    document.getElementById('profile-element').hidden = true;
    document.getElementById('button-logout').hidden = true;
  }