import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import Chats from './chats';

describe('Chats Component', () => {
  it('creates a new chat', () => {
    const { getByText, getByPlaceholderText } = render(<Chats />);
    fireEvent.click(getByText('Create a Chat'));
    fireEvent.change(getByPlaceholderText('Chat Name'), { target: { value: 'New Chat' } });
    fireEvent.click(getByText('Create'));
    expect(getByText('Chat created')).toBeInTheDocument();
  });

  it('joins a chat with an invite code', () => {
    const { getByText, getByPlaceholderText } = render(<Chats />);
    fireEvent.click(getByText('Join a Chat'));
    fireEvent.change(getByPlaceholderText('Invite Code'), { target: { value: 'ABC123' } });
    fireEvent.click(getByText('Join'));
    expect(getByText('Joining...')).toBeInTheDocument();
  });
});
