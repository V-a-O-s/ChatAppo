import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import Home from './Home';

describe('Home Component', () => {
  it('renders correctly', () => {
    const { getByText } = render(<Home />);
    expect(getByText('JChat Chatapp')).toBeInTheDocument();
  });

  it('switches to Profile view when Profile button is clicked', () => {
    render(<Home />);
    fireEvent.click(screen.getByText('Profile'));
    expect(screen.queryByText('Your Profile')).toBeInTheDocument();
  });

  it('returns to Chat view when Home button is clicked', () => {
    render(<Home />);
    fireEvent.click(screen.getByText('Profile'));
    fireEvent.click(screen.getByText('Home'));
    expect(screen.queryByText('Active Chats')).toBeInTheDocument();
  });
});
