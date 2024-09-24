-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 30, 2024 lúc 11:29 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quizapplication`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `question`
--

CREATE TABLE `question` (
  `numQuestions` int(9) NOT NULL,
  `question` text NOT NULL,
  `optionA` text NOT NULL,
  `optionB` text NOT NULL,
  `optionC` text NOT NULL,
  `optionD` text NOT NULL,
  `answer` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `question`
--

INSERT INTO `question` (`numQuestions`, `question`, `optionA`, `optionB`, `optionC`, `optionD`, `answer`) VALUES
(1, 'Try and be a little more cheerful because if you don\'t bear .......soon, you\'ll make everyone else miserable', 'through', 'along', 'up', 'to', 'up'),
(2, ' We were in a small rowing boat and were terrified that the steamer hadn\'t seen us as it was bearing .......on us', 'down', 'across', 'over', 'under', 'down'),
(3, 'I fully understand your comments and bearing those in ......., I have made the appropriate decision.', 'brain', 'mind', 'thought', 'sense', 'mind'),
(4, 'As we have all worked very hard this year, I\'m hoping that our efforts will bear ........', 'produce', 'benefits', 'yields', 'fruit', 'fruit'),
(5, 'We all have our .......to bear so I should be grateful if you would stop complaining all the time', 'problems', 'situations', 'crosses', 'results', 'crosses'),
(6, 'There is really nothing much you can do to stop it and I\'m afraid you\'ll just have to .......and bear it.', 'scorn', 'grin', 'laugh', 'smile', 'grin'),
(7, 'I hope you can be patient for a little longer and bear .......me while I try and solve the problem.', 'by', 'on', 'at', 'with', 'with'),
(8, 'She has been proved right in everything she did as the report quite clearly bears ........', 'out', 'to', 'for', 'onto', 'out'),
(9, 'The judge dismissed the new evidence completely because it had no bearing .......the trial', 'to', 'on', 'into', 'by', 'on'),
(10, 'Quite honestly the two cases are so completely different that they really don\'t bear ........', 'confirmation', 'contrast', 'comprehension', 'comparison', 'comparison');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`numQuestions`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
